resource "aws_dynamodb_table" "account_table_staging" {
  name = "TESTING_Account_Service"
  billing_mode = "PAY_PER_REQUEST"
  hash_key = "Mail"

  attribute {
    name = "Mail"
    type = "S"
  }

  tags = {
    Environment = "testing"
  }
}
