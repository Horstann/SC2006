const FileStorage = require("./FileStorage.js");

class ImageUploader {
	GetRandomChar(string) {
		return string.charAt(Math.floor(Math.random() * string.length));
	}

	GenerateId(length = 32) {
		const possibleChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

		let id = "";
		for (let i = 0; i < length; i++) {
			id += this.GetRandomChar(possibleChars);
		}
		return id;
	}

	// Accepts: base64 representation of a jpeg image.
	// Returns: A HTTP URL that will return the jpeg image.
	// Note: Please REMOVE this part: "data:image/jpeg;base64,". We do not want this part.
	async UploadImage(base64) {
		let fileStorage = new FileStorage();
		let binary = new Buffer(base64, "base64");
		let id = this.GenerateId();
		let path = "img/" + id + ".jpg";
		return fileStorage.StoreFile(binary, path);
	}
}

module.exports = ImageUploader;
