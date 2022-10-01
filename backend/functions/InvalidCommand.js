class InvalidCommand {
	ExecuteCommand(cmdData, acc, res) {
		res.send({
			"status": 5
		});
	}
}

module.exports = InvalidCommand;
