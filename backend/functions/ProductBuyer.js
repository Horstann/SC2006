const { getFirestore } = require('firebase-admin/firestore');
const { admin } = require('firebase-admin');

class ProductBuyer {
	async ExecuteCommand(cmdData, acc, res) {
		const db = getFirestore();
		const productRef = db.collection("Product").doc(cmdData.productId);
		const doc = await productRef.get();

		if (!doc.exists) res.json({"status": 7});
		if (doc.data().closingTime > admin.firestore.Timestamp.now()) res.json({"status": 11});
		if (cmdData.quantity > (doc.data().TotalUnits - doc.data().TotalBought) || cmdData.quantity < 1) res.json({"status": 12});

		let newTotalBought = doc.data().TotalBought + cmdData.quantity;
		await doc.set({
				TotalBought: newTotalBought
		});

		if (acc.BoughtProducts == null){
			boughtProduct = {};
			boughtProduct[doc.id] = cmdData.quantity;
			await acc.set({
				BoughtProducts: boughtProduct,
			}, { merge: true });
		}else{
			let newBoughtProducts = doc.data().BoughtProducts;
			newBoughtProducts[doc.id] = cmdData.quantity;
			await acc.set({
				BoughtProducts: newBoughtProducts
			});
		}
		
		res.json({"status": 7});
	}
}

module.exports = ProductBuyer;