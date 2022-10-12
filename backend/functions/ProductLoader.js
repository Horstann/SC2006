const { getFirestore } = require('firebase-admin/firestore');

class ProductLoader {
	async ExecuteCommand(cmdData, acc, res) {
		const db = getFirestore();
		const productRefs = db.collection('Product');
		const snapshot = await productRefs.get();

		let product_res = [];
		snapshot.forEach(doc => {
			product_res.push({
                "productId": doc.id,
                "name": doc.data().Name,
                "totalUnits": doc.data().TotalUnits,
                "totalBought": doc.data().TotalBought,
                "priceThresholds": doc.data().PriceThresholds,
                "unitThresholds": doc.data().UnitThresholds,
                "durationLeft": doc.data().ClosingTime,
                "desc": doc.data().Description,
                "pics": doc.data().Pictures,
            });
		});

		if (product_res.length == 0) res.json({"status": 7});
		res.json({
			"status": 0,
			"products": product_res
		});
	}
}

module.exports = ProductLoader;