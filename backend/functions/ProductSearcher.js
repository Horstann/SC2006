const { getFirestore } = require('firebase-admin/firestore');

// As query is too complex, it's unable to be pagenated.
// So function returns the full array of relevant products

class ProductSearcher {
	async ExecuteCommand(cmdData, acc, res) {
		let buyerLat = acc.HomeLocation.getLatitude();
		let buyerLong = acc.HomeLocation.getLongtitude();
		let searchTerm = cmdData.searchTerm.trim().toLowerCase();

		const db = getFirestore();
		const productRefs = db.collection('Products');
		const snapshot = await productRefs.get();

		let search_res = [];
		snapshot.forEach(doc => {
			if (doc.data().Name.trim().toLowerCase().includes(searchTerm)) {
				let sellerLat = doc.data().sellerLocation.getLatitude();
				let sellerLong = doc.data().sellerLocation.getLongtitude();
				let distanceInKm = geofire.distanceBetween([buyerLat, buyerLong], [sellerLat, sellerLong]);
				search_res.push([doc.id, distanceInKm]);
			}
		});
		if (search_res.length == 0) res.json({"status": 7});
		search_res.sort(function(a, b){return a[1]-b[1]});

		function funct(item, index, arr) {
			arr[index] = item[0];
		}
		search_res.forEach(funct)

		res.json({
			"status": 0,
			"productIds": search_res
		});
	}
}

module.exports = ProductSearcher;
