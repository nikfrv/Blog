storage "file" {
  path = "./vault-data"
}
listener "tcp" {
  address = "127.0.0.1:8200"
  tls_cert_file = "/home/nikita/cert/CA/localhost/localhost.crt"
  tls_key_file = "/home/nikita/cert/CA/localhost/localhost.decrypted.key"
}
