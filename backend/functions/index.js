const functions = require("firebase-functions");
const app = require('firebase-admin/app');
app.initializeApp({
	"storageBucket": "agora-369c3.appspot.com"
});
const Authoriser = require("./Authoriser.js");

// Create and Deploy Your First Cloud Functions
// https://firebase.google.com/docs/functions/write-firebase-functions

exports.cmd = functions.region("asia-southeast1").https.onRequest(async (req, res) => {
	//functions.logger.info("Hello logs!", {structuredData: true});
	//response.send("Hello from Firebase!");
	
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

	functions.logger.log("New request: ", req.rawBody.toString());

	global.authoriser = new Authoriser();
	await global.authoriser.CheckAuth(cmdData, res);
});
