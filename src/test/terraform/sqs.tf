resource "aws_sqs_queue" "account_sync_queue_staging" {
  name = "TESTING_Account_Service_Sync_queue"
  message_retention_seconds = 300
}
