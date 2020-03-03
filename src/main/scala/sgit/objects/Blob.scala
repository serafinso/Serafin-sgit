package sgit.objects

case class Blob(key: String, path: String) {
  def sameBlob (blob : Blob) : Boolean = { path == blob.path && key == blob.key}
}

case class BlobAndContent(key: String, path: String, content: String) {
  def sameBlob (blob : Blob) : Boolean = { path == blob.path && key == blob.key}
}
