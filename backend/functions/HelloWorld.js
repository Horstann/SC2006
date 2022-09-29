class HelloWorld {
	ExecuteCommand(cmdData, res) {
		res.json({
			"status": 0,
			"helloWorld": "Hello world from Ignis!"
		});
	}
}

module.exports = HelloWorld;
