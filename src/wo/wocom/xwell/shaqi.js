var http = require("http");
var url = require("url");

var endStrings = "";
var port = process.env.PORT || 5000;

var ReportType = {}
ReportType["/shaqi"] = shaqi;
ReportType["/AQIWarning"] = AQIWarning;
ReportType["/foreignAQI"] = foreignAQI;

http.createServer(function(request, response) {

	var pathname = url.parse(request.url).pathname;
	var clientip = request.connection.remoteAddress; //请求地址
	console.log("client_ip:" + clientip + " path_name:" + pathname);

	if (typeof ReportType[pathname] === 'function') {
		//如果handle[pathname]是函数
		ReportType[pathname](response);
		//执行对应函数
	} else {
		return200(response, "null");
	}

}).listen(port);

//http://www.infoq.com/cn/articles/nodejs-about-buffer/
var http_proxy = function(host, callback) {
	http.get(host,
	function(res) {

		var buffers = [];
		var nread = 0;

		res.on('data',
		function(chunk) {
			buffers.push(chunk);
			nread += chunk.length;
		});

		res.on('end',
		function() {

			var buffer = null;

			switch (buffers.length) {
			case 0:
				buffer = new Buffer(0);
				break;
			case 1:
				buffer = buffers[0];
				break;
			default:
				buffer = new Buffer(nread);
				for (var i = 0,
				pos = 0,
				l = buffers.length; i < l; i++) {
					var chunk = buffers[i];
					chunk.copy(buffer, pos);
					pos += chunk.length;
				}
				break;
			}

			callback(buffer.toString());
		});

	}).on('error',
	function(e) {
		console.log("Got error: " + e.message);
	});

}

function return200(response, str) {

	response.writeHead(200, "");
	response.write("" + str);
	response.end();
	buffer = "";
}

function shaqi(response) {

	http_proxy("http://www.semc.gov.cn/aqi/home/Index.aspx",
	function(str) {

		var regex = /实时空气质量状况[\s\S]*发布+?/;

		if (regex.test(str)) {
			var match = str.match(regex);
			match = match[0].replace(/<[^>]*>/g, "").replace(/\s/g, "");
			match = match.replace(/。(空.*。关闭)/g, "").replace(/&.*?;/g, "");
			match = match.replace(/发布.*/g, "");
			//console.log(match); 
			return200(response, match);
		} else {
			console.log("no match");
			return200(response, "");
		}

	});
}

function AQIWarning(response) {

	http_proxy("http://www.semc.gov.cn/home/index.aspx",
	function(str) {

		var regex = /预警[\s\S]+?table>/;

		if (regex.test(str)) {
			var match = str.match(regex);
			match = match[0].replace(/<[^>]*>/g, "").replace(/\s/g, "");
			match = match.replace(/&.*?;/g, ",");
			return200(response, match);
		} else {
			console.log("no match");
			return200(response, "");
		}

	});

}

function foreignAQI(response) {

	http_proxy("http://shaqi.info/m/shanghai/mobile",
	function(str) {
		return200(response, "");
	});

}