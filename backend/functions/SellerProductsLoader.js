const { getFirestore } = require('firebase-admin/firestore');

class SellerProductsLoader {
	async ExecuteCommand(cmdData, acc, res) {
		if (acc == null) {
			res.json({"status": 6});
			return;
		}

		const db = getFirestore();
		const productRefs = db.collection('Product');
		const snapshot = await productRefs.get();

		let products = [];
		for (const doc of snapshot.docs) {
			const sellerId = doc.data().Seller.id;

			if (sellerId == acc.id){
				let timestamp = doc.data().ClosingTime;
				let date = timestamp.toDate();
				let date1 = [date.getDate().toString().padStart(2,'0'),(date.getMonth() + 1).toString().padStart(2,'0'),date.getFullYear(),].join('/')
				let date2 = date.getHours().toString().padStart(2,'0') + ":" + date.getMinutes().toString().padStart(2,'0')
				let dateString = date1 + " " + date2;

				products.push({
						"productId": doc.id,
						"name": doc.data().Name,
						"totalUnits": doc.data().TotalUnits,
						"totalBought": doc.data().TotalBought,
						"priceThresholds": doc.data().PriceThresholds,
						"unitThresholds": doc.data().UnitThresholds,
						"closingTime": dateString,
						"desc": doc.data().Description,
						"pics": doc.data().Pictures,
				});
			}
		}

		if (products.length == 0) {
			res.json({"status": 7});
			return;
		}

		res.json({
			"status": 0,
			"products": products
		});
	}
}

module.exports = SellerProductsLoader;