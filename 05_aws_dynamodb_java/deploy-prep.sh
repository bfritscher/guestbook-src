#!/bin/bash

# Build the application
echo "Building Spring Boot application..."
./mvnw clean package -DskipTests

# Check if build was successful
if [ $? -eq 0 ]; then
    echo "✅ Build successful!"
    echo "JAR file created: target/guestbook-0.0.1-SNAPSHOT.jar"
    echo ""
    
    # Create deployment package with JAR at root level
    echo "Creating Elastic Beanstalk deployment package..."
    
    # Copy JAR to root level with a standard name
    cp target/guestbook-0.0.1-SNAPSHOT.jar ./application.jar
    
    # Create deployment zip with JAR at root level
    zip -r guestbook-eb.zip application.jar .ebextensions/ .platform/
    
    echo "✅ Deployment package created: guestbook-eb.zip"
    echo ""
    echo "🚀 Ready for Elastic Beanstalk deployment!"
    echo ""
    echo "Deployment options:"
    echo "1. Upload guestbook-eb.zip to EB (contains JAR at root + configs)"
    echo "2. Upload application.jar directly to EB"
    echo ""
    echo "⚠️  Make sure your EB environment has:"
    echo "   - Java 21 platform"
    echo "   - IAM role with DynamoDB permissions"
    echo "   - guestbook.service=dynamodb environment variable"
    
    # Clean up
    rm -f application.jar
else
    echo "❌ Build failed!"
    exit 1
fi
