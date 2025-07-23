#!/bin/bash
# Installs on Debian/Ubuntu VM
# Check if running as root
if [ "$EUID" -ne 0 ]; then
    echo "Error: This script must be run as root (use sudo)"
    exit 1
fi

# Make sure script is called with DNS name desired
if [ $# -ne 1 ]; then
    echo "Usage: sudo install.sh <DNS_Name>"
    echo "Example: sudo install.sh myapp.example.com"
    exit 1
fi

DNS_NAME="$1"

# Check that domain name contains at least one dot (is FQDN)
if [[ "$DNS_NAME" != *.* ]]; then
    echo "Error: Domain name must be fully qualified (contain at least one dot)"
    echo "Example: myapp.example.com, not just 'myapp'"
    exit 1
fi

# Name the service based on first part of DNS name
SITE=`echo $1 | sed -e 's/\..*//' | tr '-' '_'`

# Install all required system packages
apt-get update
apt-get install -y nginx python3 python3-pip python3-venv nginx certbot python3-certbot-nginx

# Install all required python packages
python3 -m venv env
source env/bin/activate
pip3 install --upgrade -r requirements.txt

# Set up systemd service for site
sed s+PROJECT_USER+$SUDO_USER+ etc/systemd.template | sed s+PROJECT_DIR+$PWD+ > /etc/systemd/system/$SITE.service

# Configure nginx for site
sed s+PROJECT_HOST+$1+ etc/nginx.template | sed s+PROJECT_DIR+$PWD+ > /etc/nginx/sites-available/$SITE
ln -s /etc/nginx/sites-available/$SITE /etc/nginx/sites-enabled

# Restart all services
systemctl start $SITE
systemctl enable $SITE
systemctl restart nginx

echo "Installation complete."
