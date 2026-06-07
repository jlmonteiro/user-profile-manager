#!/bin/bash
set -e

# Colors and icons
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m'

info()  { echo -e "${BLUE}ℹ️  $1${NC}"; }
ok()    { echo -e "${GREEN}✅ $1${NC}"; }
err()   { echo -e "${RED}❌ $1${NC}"; exit 1; }
step()  { echo -e "\n${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"; echo -e "${BLUE}🔧 $1${NC}"; echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"; }

# Configuration
PROJECT_DIR="$HOME/user-profile-manager"
NAMESPACE="user-manager"
RELEASE_NAME="user-manager"
BACKEND_IMAGE="ghcr.io/jlmonteiro/user-profile-manager/backend"
FRONTEND_IMAGE="ghcr.io/jlmonteiro/user-profile-manager/frontend"
VERSION=$(grep "^appVersion:" "$PROJECT_DIR/helm-deployment/Chart.yaml" | tr -d '"' | awk '{print $2}')

# Database settings
DB_URL="jdbc:postgresql://internal-postgresql-rw.postgres:5432/homedb?currentSchema=user_manager"
DB_USERNAME="home_user"
DB_PASSWORD="home_user"

export KUBECONFIG="$HOME/.kube/config"

echo -e "\n${GREEN}🚀 User Profile Manager — Local Deploy${NC}"
echo -e "${GREEN}   Version: ${VERSION}${NC}\n"

# Step 1: Build backend image (Quarkus container-image-docker)
step "Building backend image"
cd "$PROJECT_DIR"
./gradlew :backend:build -Dquarkus.container-image.build=true \
  -Dquarkus.container-image.tag="$VERSION" || err "Backend build failed"
ok "Backend image built: $BACKEND_IMAGE:$VERSION"

# Step 2: Build frontend image
step "Building frontend image"
docker build -f "$PROJECT_DIR/frontend/frontend.dockerfile" \
  -t "$FRONTEND_IMAGE:$VERSION" \
  "$PROJECT_DIR/frontend" || err "Frontend build failed"
ok "Frontend image built: $FRONTEND_IMAGE:$VERSION"

# Step 3: Import images into k3s
step "Importing images into k3s"
docker save "$BACKEND_IMAGE:$VERSION" | sudo k3s ctr images import - || err "Failed to import backend image"
ok "Backend image imported"
docker save "$FRONTEND_IMAGE:$VERSION" | sudo k3s ctr images import - || err "Failed to import frontend image"
ok "Frontend image imported"

# Step 4: Create namespace
step "Preparing namespace"
kubectl create namespace "$NAMESPACE" --dry-run=client -o yaml | kubectl apply -f -
ok "Namespace $NAMESPACE ready"

# Step 5: Deploy with Helm
step "Deploying with Helm"
helm upgrade --install "$RELEASE_NAME" "$PROJECT_DIR/helm-deployment" \
  --namespace "$NAMESPACE" \
  --set backend.image.tag="$VERSION" \
  --set frontend.image.tag="$VERSION" \
  --set backend.config.DB_URL="$DB_URL" \
  --set backend.config.DB_USERNAME="$DB_USERNAME" \
  --set secrets.databasePassword="$DB_PASSWORD" \
  --wait --timeout 120s || err "Helm deploy failed"
ok "Deployed version $VERSION to namespace $NAMESPACE"

echo -e "\n${GREEN}🎉 Deploy complete!${NC}"
