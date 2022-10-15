const ImageUploader = require("./ImageUploader.js");

class ImageUploaderTester {
	async ExecuteCommand(cmdData, acc, res) {
		res.json({
			"status": 0,
			"url": await new ImageUploader().UploadImage(cmdData.pic)
		});
	}
}

module.exports = ImageUploaderTester;
