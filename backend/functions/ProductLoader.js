const { getFirestore, Timestamp } = require('firebase-admin/firestore');
const functions = require("firebase-functions");

class ProductLoader {
	async ExecuteCommand(cmdData, acc, res) {
		if (acc == null) res.json({"status": 6});

		let buyerLat = acc.data().HomeLocation.latitude;
		let buyerLong = acc.data().HomeLocation.longitude;

		const db = getFirestore();
		const productRefs = db.collection("Product");
		const snapshot = await productRefs.get();
		functions.logger.log("IT'S HERE BOIS " + snapshot.size);
		
		if (snapshot.empty) res.json({"status": 7});

		let products = [];
		await snapshot.forEach(async doc => {

			const sellerId = doc.data().Seller.id;
			const sellerRef = db.collection("User").doc(sellerId);
			const sellerDoc = await sellerRef.get();
			functions.logger.log("SELLERDOC " + doc.id + ": " + sellerId + ": " + JSON.stringify(sellerDoc.data()));

			products.push(sellerDoc.data().HomeLocation);

			/*
			let sellerLat = sellerDoc.data().HomeLocation.latitude;
			let sellerLong = sellerDoc.data().HomeLocation.longitude;
			let distanceInKm = Math.sqrt(((buyerLat-sellerLat)*110.547)**2 + (111.320*Math.cos(buyerLong-sellerLong))**2);
			
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
				"distanceFromUser": distanceInKm
            });
			*/
		});

		res.json({
			"status": 0,
			"products": products
		});
	}
}

module.exports = ProductLoader;