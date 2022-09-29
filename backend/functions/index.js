const CommandHub = require("./CommandHub.js");
const functions = require("firebase-functions");
const { initializeApp } = require('firebase-admin/app');

// Create and Deploy Your First Cloud Functions
// https://firebase.google.com/docs/functions/write-firebase-functions

exports.cmd = functions.region("asia-southeast1").https.onRequest(async (req, res) => {
	//functions.logger.info("Hello logs!", {structuredData: true});
	//response.send("Hello from Firebase!");

	initializeApp();
	
	let cmdData;
	try {
		cmdData = JSON.parse(req.rawBody);
	}
	catch (e) {
		if (e instanceof SyntaxError) {
			cmdData = {};
		}
		else {
			throw e;
		}
	}
	
	let commandHub = new CommandHub();
	let cmd = commandHub.DecodeCommand(cmdData.cmd);

	await cmd.ExecuteCommand(cmdData, res);
});
