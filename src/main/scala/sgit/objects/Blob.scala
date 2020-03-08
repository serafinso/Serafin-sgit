package sgit.objects

/** The Blob class
 *
 * @param key the blob key
 * @param path the path key
 */
case class Blob(key: String, path: String) {
  def sameBlob (blob : Blob) : Boolean = { path == blob.path && key == blob.key}
}
