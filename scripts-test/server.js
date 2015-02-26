var fs = require('fs');
var path = require('path');
var express = require('express');

var PORT = 3000;
var HOST = 'http://localhost:' + PORT;

var app = express();

app.configure(function() {
	app.use(express.bodyParser());
	app.use(app.router);
});

app.get('/', function (req, res) {
	res.send('Available routes: POST /j_spring_security_check, POST /plugin/uploadPlugin');
});

app.post('/j_spring_security_check', function (req, res) {
	console.log();
	console.log(req.body);
	res.set('Location', HOST + '/login_result');
	res.send(302);
});

app.post('/plugin/uploadPlugin', function (req, res) {
	console.log();
	console.log(req.body);
	console.log(req.files);

	var html = '<p>pluginId: ' + req.body.pluginId + '</p>' +
		'<p>notes:</p>' +
		req.body.notes +
		'<p>file: ' +
		req.files.file.originalFilename + ' -&gt; ' + req.files.file.path +
		'</p>';

	fs.writeFile('out.html', html, function (err) {
		if (err) {
			console.error(err);
		}
		else {
			console.log();
			console.log('You can check that release notes are ready for inclusion in ' +
				'an HTML document by typing: `open out.html`');
		}
		res.set('Location', HOST + '/plugin/7105');
		res.send(302);
	});
});

app.listen(PORT, function () {
	console.log('App started at ' + HOST);
});

