class HelloWorld {
	ExecuteCommand(cmdData, acc, res) {
		if (acc == null) {
			res.json({
				"status": 0,
				"helloWorld": "Hello world from an anonymous account!"
			});
		}
		else {
			res.json({
				"status": 0,
				"helloWorld": "Hello world from account with home address: " + acc.data().HomeAddress + "!"
			});
		}
	}
}

module.exports = HelloWorld;
