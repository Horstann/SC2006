class SignInController {
	ExecuteCommand(cmdData, acc, res) {
		if (acc == null) {
			res.json({"status": 6});
			return;
		}

		res.json({"status": 0});
	}
}

module.exports = SignInController;
