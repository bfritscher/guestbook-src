# K3s and Rancher Deployment

## Prerequisites
Install the K3s Ansible collection:
```bash
ansible-galaxy collection install git+https://github.com/k3s-io/k3s-ansible.git
```

## Deployment Steps

### 1. Provision VMs on OpenStack

Update the variables in `inventory/config`:
- `os_key_name`: Set to your SSH key pair name from SWITCHengines

```bash
ansible-playbook provision.yml
```

### 2. Install K3s cluster
```bash
ansible-playbook k3s.orchestration.site -i inventory
```

### 3. Install Rancher With Let's Encrypt certificates
```bash
ansible-playbook install-rancher.yml -i inventory
```

## Configuration

Update the variables in `inventory/config`:
- `rancher_hostname`: Set to your domain name
- `rancher_bootstrap_password`: Set a strong password for initial setup

## Access Rancher

After installation, access Rancher at: `https://your-rancher-hostname`
Use the bootstrap password for the first login, then set up your admin account.
