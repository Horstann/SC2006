const HelloWorld = require("./HelloWorld.js");
const SignUpController = require("./SignUpController.js");
const SignInController = require("./SignInController.js");
const AccountEditor = require("./AccountEditor.js");
const ProductAdder = require("./ProductAdder");
const ProductLoader = require("./ProductLoader.js");
const ProductViewer = require("./ProductViewer.js");
const ProductSearcher = require("./ProductSearcher");
const ProductEditor = require("./ProductEditor.js");
const InvalidCommand = require("./InvalidCommand.js");
const ImageUploaderTester = require("./ImageUploaderTester.js");
const QRCodeLoader = require("./QRCodeLoader.js");
const BuyerProductsLoader = require("./BuyerProductsLoader");
const SellerProductsLoader = require("./SellerProductsLoader");
const ProductBuyer = require("./ProductBuyer");
const ProductDropper = require("./ProductDropper");
const ClosingTimeChecker = require("./ClosingTimeChecker");

class CommandHub {
	async DecodeCommand(cmdData, acc, res) {
		let cmd;
		switch (cmdData.cmd) {
			case "helloWorld":	cmd = new HelloWorld(); break;
			case "signUp":		cmd = new SignUpController(); break;
			case "signIn":		cmd = new SignInController(); break;
			case "editAccount":	cmd = new AccountEditor(); break;
			case "addProduct":	cmd = new ProductAdder(); break;
			case "loadProducts": 	cmd = new ProductLoader(); break;
			case "viewProduct": 	cmd = new ProductViewer(); break;
			case "searchProducts":	cmd = new ProductSearcher(); break;
			case "editProduct": 	cmd = new ProductEditor(); break;
			case "testImageUpload": cmd = new ImageUploaderTester(); break;
			case "loadQRCode":		cmd = new QRCodeLoader(); break;
			case "loadBuyerProducts": cmd = new BuyerProductsLoader(); break;
			case "loadSellerProducts": cmd = new SellerProductsLoader(); break;
			case "buyProduct":		cmd = new ProductBuyer(); break;
			case "dropProduct":		cmd = new ProductDropper(); break;
			case "checkClosingTime": cmd = new ClosingTimeChecker(); break;
			default:		cmd = new InvalidCommand(); break;
		}

		await cmd.ExecuteCommand(cmdData, acc, res);
	}
}

module.exports = CommandHub;
