class SignInController {
	ExecuteCommand(cmdData, acc, res) {
		if (acc == null) {
			res.json({"status": 6});
		}
		else {
			res.json({"status": 0});
		}
	}
}

module.exports = SignInController;
