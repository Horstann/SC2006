const auth = require("google-auth-library");
const firestore = require('firebase-admin/firestore');
const CommandHub = require("./CommandHub.js");

const CLIENT_ID = "143232307943-mnfh1u4sve8f166u8uvtntkntn5qa2uv.apps.googleusercontent.com";

class Authoriser {
	async GetUidFromAuthKey(authKey) {
		let client = new auth.OAuth2Client(CLIENT_ID);
		try {
			let ticket = await client.verifyIdToken({
				"idToken": authKey,
				"audience": CLIENT_ID
			});
			return ticket.getUserId();
		}
		catch {
			return null;
		}
	}

	async GetAccFromUid(uid) {
		let db = firestore.getFirestore();
		let accs = await db.collection("User").where("UID", "==", uid).get();
		if (accs.empty) {
			return null;
		}
		return accs.docs[0];
	}

	async CheckAuth(cmdData, res) {
		if (cmdData.authKey == undefined) {
			res.json({"status": 9});
			return;
		}

		if (cmdData.authKey == "") {
			await new CommandHub().DecodeCommand(cmdData, null, res);
			return;
		}

		// Get uid from authKey
		let uid = await this.GetUidFromAuthKey(cmdData.authKey);
		if (uid == null) {
			res.json({"status": 1});
			return;
		}

		// Get an account from our database using uid
		let acc = await this.GetAccFromUid(uid);
		if (acc == null) {
			res.json({"status": 2});
			return;
		}
		
		await new CommandHub().DecodeCommand(cmdData, acc, res);
	}
}

module.exports = Authoriser;
