const { getFirestore, Timestamp } = require('firebase-admin/firestore');
const { getDistance } = require('geolib');
const functions = require("firebase-functions");

class ProductLoader {
	async ExecuteCommand(cmdData, acc, res) {
		if (acc == null) res.json({"status": 6});

		let buyerLat = acc.data().HomeLocation.latitude;
		let buyerLong = acc.data().HomeLocation.longitude;

		const db = getFirestore();
		const productRefs = db.collection("Product");
		const snapshot = await productRefs.get();
		//functions.logger.log("IT'S HERE BOIS " + snapshot.size);
		
		if (snapshot.empty) res.json({"status": 7});

		let products = [];
		for (const doc of snapshot.docs) {
			const sellerId = doc.data().Seller.id;
			const sellerRef = db.collection("User").doc(sellerId);
			const sellerDoc = await sellerRef.get();
			//functions.logger.log("SELLERDOC " + doc.id + ": " + sellerId + ": " + JSON.stringify(sellerDoc.data()));

			//products.push(sellerDoc.data().HomeLocation);
			//functions.logger.log("ADDING: " + sellerDoc.data().HomeLocation);

			let sellerLat = sellerDoc.data().HomeLocation.latitude;
			let sellerLong = sellerDoc.data().HomeLocation.longitude;

			let distanceInKm = getDistance(
				{ latitude: buyerLat, longitude: buyerLong },
				{ latitude: sellerLat, longitude: sellerLong }
			)
			distanceInKm /= 1000;
			/*
			const GeoPoint = require('geopoint');
			let buyerPoint = new GeoPoint(buyerLat, buyerLong);
			let sellerPoint = new GeoPoint(sellerLat, sellerLong);
			var distanceInKm = buyerPoint.distanceTo(sellerPoint, true)//output in kilometers
			*/

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
				"sellerAddress": sellerDoc.data().HomeAddress,
				"sellerLat": sellerLat,
				"sellerLong": sellerLong,
				"distanceFromUser": distanceInKm
			});
		};

		//functions.logger.log("Done");

		res.json({
			"status": 0,
			"products": products
		});
	}
}

module.exports = ProductLoader;