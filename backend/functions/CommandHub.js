const HelloWorld = require("./HelloWorld.js");
const SignUpController = require("./SignUpController.js");
const SignInController = require("./SignInController.js");
const AccountEditor = require("./AccountEditor.js");
const ProductLoader = require("./ProductLoader.js");
const ProductViewer = require("./ProductViewer.js");
const ProductSearcher = require("./ProductSearcher");
const ProductAdder = require("./ProductAdder");
const InvalidCommand = require("./InvalidCommand.js");

class CommandHub {
	async DecodeCommand(cmdData, acc, res) {
		let cmd;
		switch (cmdData.cmd) {
			case "helloWorld":	cmd = new HelloWorld(); break;
			case "signUp":		cmd = new SignUpController(); break;
			case "signIn":		cmd = new SignInController(); break;
			case "editAccount":	cmd = new AccountEditor(); break;
			case "loadProducts": cmd = new ProductLoader(); break;
			case "viewProduct": cmd = new ProductViewer(); break;
			case "searchProducts":	cmd = new ProductSearcher(); break;
			case "addProduct":	cmd = new ProductAdder(); break;
			default: cmd = new InvalidCommand(); break;
		}

		await cmd.ExecuteCommand(cmdData, acc, res);
	}
}

module.exports = CommandHub;
