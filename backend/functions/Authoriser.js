const auth = require("google-auth-library");
const firestore = require('firebase-admin/firestore');
const CommandHub = require("./CommandHub.js");

const CLIENT_ID = "TODO";

class Authoriser {
	async CheckAuth(cmdData, res) {
		if (cmdData.authKey == undefined || cmdData.authKey == "") {
			await new CommandHub().DecodeCommand(cmdData, null, res);
			return;
		}

		// Get uid from authKey
		let uid = await GetUidFromAuthKey(cmdData.authKey);
		if (uid == null) {
			res.json({"status": 1});
			return;
		}

		// Get an account from our database using uid
		let db = firestore.getFirestore();
		let accs = await db.collection("User").where("UID", "==", uid).get();
		if (accs.empty) {
			res.json({"status": 2});
			return;
		}

		let acc = accs[0].data();
		await new CommandHub().DecodeCommand(cmdData, acc, res);
	}

	async GetUidFromAuthKey(authKey) {
		let client = new auth.OAuth2Client(CLIENT_ID);
		let ticket = await client.verifyIdToken({
			"idToken": authKey,
			"audience": CLIENT_ID
		});
		return ticket.getUserId();
	}
}

module.exports = Authoriser;
