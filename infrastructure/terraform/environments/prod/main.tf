provider "aws" {
  region = var.aws_region
}

terraform {
  required_version = ">= 1.0"

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }

  backend "s3" {
    bucket = "ecommerce-terraform-state"
    key    = "prod/terraform.tfstate"
    region = "us-east-1"
  }
}

module "vpc" {
  source = "../../modules/vpc"

  vpc_name    = "${var.environment}-vpc"
  vpc_cidr    = var.vpc_cidr
  cluster_name = local.cluster_name

  availability_zones   = var.availability_zones
  private_subnet_cidrs = var.private_subnet_cidrs
  public_subnet_cidrs  = var.public_subnet_cidrs

  tags = local.tags
}

module "eks" {
  source = "../../modules/eks"

  cluster_name     = local.cluster_name
  cluster_version  = var.cluster_version
  vpc_id          = module.vpc.vpc_id
  private_subnet_ids = module.vpc.private_subnets

  min_size      = var.min_size
  max_size      = var.max_size
  desired_size  = var.desired_size
  instance_types = var.instance_types

  tags = local.tags
}

locals {
  cluster_name = "${var.environment}-${var.project_name}"
  tags = {
    Environment = var.environment
    Project     = var.project_name
    ManagedBy   = "terraform"
  }
} 