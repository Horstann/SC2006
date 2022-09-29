class InvalidCommand {
	ExecuteCommand(cmdData, res) {
		res.send({
			"status": 5
		});
	}
}

module.exports = InvalidCommand;
