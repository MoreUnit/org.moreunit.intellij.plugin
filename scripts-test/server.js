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
	res.send('Available routes: POST /j_spring_security_check, GET /plugin/edit, POST /plugin/uploadPlugin');
});

app.post('/j_spring_security_check', function (req, res) {
	console.log('/j_spring_security_check');
	console.log(req.body);
	res.set('Location', HOST + '/login_result');
	res.send(302);
});

app.get('/plugin/edit', function (req, res) {
	console.log('/plugin/edit');
	console.log(req.query);
  res.send('<input type="hidden" name="com.jetbrains.pluginSite.SYNCHRONIZER_TOKEN" value="f4a12d4e-4165-49c3-820f-7970f678ed18"/>');
});

app.post('/plugin/uploadPlugin', function (req, res) {
	console.log('/plugin/uploadPlugin');
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

