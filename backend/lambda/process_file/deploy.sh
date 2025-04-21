#!/bin/bash

ACCOUNT_ID="859059810646"
REGION="us-east-1"
REPO_NAME="ocr-lambda"
IMAGE_TAG="latest"
IMAGE_NAME="$REPO_NAME:$IMAGE_TAG"
ECR_URI="$ACCOUNT_ID.dkr.ecr.$REGION.amazonaws.com/$REPO_NAME:$IMAGE_TAG"

DOCKER_BUILDKIT=1 docker buildx build --platform linux/amd64 -t $IMAGE_NAME .
if [ $? -ne 0 ]; then
  echo "Docker build failed"
  exit 1
fi

aws ecr get-login-password --region $REGION \
  | docker login --username AWS --password-stdin $ACCOUNT_ID.dkr.ecr.$REGION.amazonaws.com
if [ $? -ne 0 ]; then
  echo "Docker login failed"
  exit 1
fi

docker tag $IMAGE_NAME $ECR_URI
docker push $ECR_URI
if [ $? -ne 0 ]; then
  echo "Docker push failed"
  exit 1
fi
