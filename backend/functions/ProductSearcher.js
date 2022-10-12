const { getFirestore } = require('firebase-admin/firestore');
const { admin } = require('firebase-admin');

// As query is too complex, it's unable to be pagenated.
// So function returns the full array of relevant products

class ProductSearcher {
	async ExecuteCommand(cmdData, acc, res) {
		let buyerLat = acc.data().HomeLocation.getLatitude();
		let buyerLong = acc.data().HomeLocation.getLongtitude();
		let searchTerm = cmdData.searchTerm.trim().toLowerCase();

		const db = getFirestore();
		const productRefs = db.collection('Product');
		const snapshot = await productRefs.get();

		let search_res = [];
		snapshot.forEach(doc => {
			if (doc.data().Name.trim().toLowerCase().includes(searchTerm) && doc.data().closingTime > admin.firestore.Timestamp.now()) {
				let sellerLat = doc.data().Seller.data().HomeLat;
				let sellerLong = doc.data().Seller.data().HomeLong;
				let distanceInKm = geofire.distanceBetween([buyerLat, buyerLong], [sellerLat, sellerLong]);
				search_res.push([{
					"productId": doc.id,
					"name": doc.data().Name,
					"totalUnits": doc.data().TotalUnits,
					"totalBought": doc.data().TotalBought,
					"priceThresholds": doc.data().PriceThresholds,
					"unitThresholds": doc.data().UnitThresholds,
					"durationLeft": doc.data().ClosingTime,
					"desc": doc.data().Description,
					"pics": doc.data().Pictures,
				}, distanceInKm]);
			}
		});
		if (search_res.length == 0) res.json({"status": 7});
		search_res.sort(function(a, b){return a[1]-b[1]});

		function funct(item, index, arr) {
			arr[index] = item[0];
		}
		search_res.forEach(funct);

		res.json({
			"status": 0,
			"products": search_res
		});
	}
}

module.exports = ProductSearcher;
/*
{
    "cmd": "addProduct",
    "authKey": "",
    "name": "A Cow",
    "totalUnits": 10,
    "priceThresholds": [100, 85, 75, 65, 50],
    "unitThresholds": [0, 2, 4, 6, 10],
    "desc": "Yep, a cow",
    "closingTime": "0001-01-01T00:00:00Z",
    "pics": ["https://img.fruugo.com/product/0/70/7700700_max.jpg", "https://upload.wikimedia.org/wikipedia/commons/0/0c/Cow_female_black_white.jpg"]
}
*/