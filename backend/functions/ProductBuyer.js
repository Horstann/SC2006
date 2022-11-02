const { getFirestore, Timestamp } = require('firebase-admin/firestore');
const functions = require("firebase-functions");

class ProductBuyer {
	async ExecuteCommand(cmdData, acc, res) {
		if (cmdData.productId == undefined || cmdData.quantity == undefined) {
			res.json({"status": 9});
			return;
		}

		if (acc == null) {
			res.json({"status": 6});
			return;
		}

		const db = getFirestore();
		const productRef = db.collection("Product").doc(cmdData.productId);
		const doc = await productRef.get();

		if (!doc.exists) {
			res.json({"status": 7});
			return;
		}
		if (doc.data().ClosingTime.toMillis() < Timestamp.now().toMillis()) {
			res.json({"status": 11});
			return;
		}
		if (cmdData.quantity > (doc.data().TotalUnits - doc.data().TotalBought) || cmdData.quantity < 1) {
			res.json({"status": 12});
			return;
		}

		let newTotalBought = doc.data().TotalBought + cmdData.quantity;
		await doc.ref.set({
				TotalBought: newTotalBought
		}, { merge: true });

		if (acc.data().BoughtProducts == undefined){
			functions.logger.log("New.");
			let boughtProduct = {};
			boughtProduct[doc.id] = cmdData.quantity;
			await acc.ref.set({
				BoughtProducts: boughtProduct,
			}, { merge: true });
		}
		else {
			let newBoughtProducts = acc.data().BoughtProducts;
			newBoughtProducts[doc.id] = (newBoughtProducts[doc.id] ?? 0) + cmdData.quantity;
			functions.logger.log("Adding: " + newBoughtProducts[doc.id] ?? 0);
			await acc.ref.set({
				BoughtProducts: newBoughtProducts
			}, { merge: true });
		}
		
		res.json({"status": 0});
	}
}

module.exports = ProductBuyer;