const { getFirestore } = require('firebase-admin/firestore');
const { admin } = require('firebase-admin');

class SellerProductsLoader {
	async ExecuteCommand(cmdData, acc, res) {

		const db = getFirestore();
		const productRefs = db.collection('Product');
		const snapshot = await productRefs.get();

		let products = [];
		snapshot.forEach(async doc => {
            const sellerId = doc.data().Seller.id;

            if (sellerId = acc.id){
                products.push({
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
			}
		});

		if (products.length == 0) res.json({"status": 7});

		res.json({
			"status": 0,
			"products": products
		});
	}
}

module.exports = SellerProductsLoader;