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
ansible-playbook -i inventory provision.yml
```

### 2. Install K3s cluster

```bash
ansible-playbook -i inventory k3s.orchestration.site
```

### 3. Install Rancher With Let's Encrypt certificates

Update the variables in `inventory/config`:
- `rancher_hostname`: Set to your domain name (make sure the server IP from the previous step is correctly pointed to this domain)
- `rancher_bootstrap_password`: Set a strong password for initial setup (you will use this to log in to Rancher for the first time)

```bash
ansible-playbook -i inventory install-rancher.yml
```

## Access Rancher

After installation, access Rancher at: `https://your-rancher-hostname`
Use the bootstrap password for the first login, then set up your admin account.
