terraform {
  required_providers {
    aws = {
      source = "hashicorp/aws"
      version = "3.35.0"
    }
  }
}

provider "aws" {
  region = "us-east-1"
  endpoints {
    dynamodb = "http://localhost:8000"
    sqs = "http://localhost:4566"
  }
}