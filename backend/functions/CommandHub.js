const HelloWorld = require("./HelloWorld.js");
const InvalidCommand = require("./InvalidCommand.js");

class CommandHub {
	DecodeCommand(cmdName) {
		let cmd;
		switch (cmdName) {
			case "helloWorld":	cmd = new HelloWorld(); break;
			default:		cmd = new InvalidCommand(); break;
		}
		return cmd;
	}
}

module.exports = CommandHub;
