const storage = require("firebase-admin/storage");

class FileStorage {
	async StoreFile(binary, path) { // "binary" is a NodeJS Buffer object.
		storage.getStorage().bucket().file(path).save(binary);
		return "http://storage.googleapis.com/agora-369c3.appspot.com/" + path;
	}
}

module.exports = FileStorage;
