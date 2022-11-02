const { getFirestore, Timestamp } = require('firebase-admin/firestore');

class BuyerProductsLoader {
	async ExecuteCommand(cmdData, acc, res) {
		let buyerLat = acc.data().HomeLocation.latitude;
		let buyerLong = acc.data().HomeLocation.longitude;

		const db = getFirestore();
		const productRefs = db.collection('Product');
		const snapshot = await productRefs.get();

		let products = [];
		for (const doc of snapshot.docs) {
			for (const boughtProduct of acc.data().BoughtProducts) { // yes this is unoptimised af, sue me
				if (doc.id == boughtProduct.id) {
					let unitsBought = acc.data().BoughtProducts[doc.id];

					const sellerId = doc.data().Seller.id;
					const sellerRef = db.collection("User").doc(sellerId);
					const sellerDoc = await sellerRef.get();
					let sellerLat = sellerDoc.data().HomeLocation.latitude;
					let sellerLong = sellerDoc.data().HomeLocation.longitude;
					let distanceInKm = Math.sqrt(((buyerLat-sellerLat)*110.547)**2 + (111.320*Math.cos(buyerLong-sellerLong))**2);
					
					let isClosed = false;
					if (doc.data().ClosingTime.toMillis() < Timestamp.now().toMillis()) isClosed = true;

					let timestamp = doc.data().ClosingTime;
					let date = timestamp.toDate();
					let date1 = [date.getDate().toString().padStart(2,'0'),(date.getMonth() + 1).toString().padStart(2,'0'),date.getFullYear(),].join('/')
					let date2 = date.getHours().toString().padStart(2,'0') + ":" + date.getMinutes().toString().padStart(2,'0')
					let dateString = date1 + " " + date2;

					products.push([{
						"productId": doc.id,
						"name": doc.data().Name,
						"unitsBought": unitsBought,
						"totalUnits": doc.data().TotalUnits,
						"totalBought": doc.data().TotalBought,
						"priceThresholds": doc.data().PriceThresholds,
						"unitThresholds": doc.data().UnitThresholds,
						"closingTime": dateString,
						"desc": doc.data().Description,
						"pics": doc.data().Pictures,
						"distanceFromUser": distanceInKm,
						"isClosed": isClosed
					}, distanceInKm]);
				}
			}
		}

		if (products.length == 0) {
			res.json({"status": 7});
			return;
		}

		products.sort(function(a, b){return a[1]-b[1]});

		function funct(item, index, arr) {
			arr[index] = item[0];
		}
		products.forEach(funct);

		res.json({
			"status": 0,
			"products": products
		});
	}
}

module.exports = BuyerProductsLoader;