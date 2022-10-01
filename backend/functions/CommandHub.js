const HelloWorld = require("./HelloWorld.js");
const SignUpController = require("./SignUpController.js");
const SignInController = require("./SignInController.js");
const ProductViewer = require("./ProductViewer.js");
const InvalidCommand = require("./InvalidCommand.js");

class CommandHub {
	async DecodeCommand(cmdData, acc, res) {
		let cmd;
		switch (cmdData.cmd) {
			case "helloWorld":	cmd = new HelloWorld(); break;
			case "signUp":		cmd = new SignUpController(); break;
			case "signIn":		cmd = new SignInController(); break;
			case "viewProduct":	cmd = new ProductViewer(); break;
			default:		cmd = new InvalidCommand(); break;
		}

		await cmd.ExecuteCommand(cmdData, acc, res);
	}
}

module.exports = CommandHub;
