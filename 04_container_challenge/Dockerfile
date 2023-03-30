FROM ubuntu:22.04
RUN apt-get update -y
RUN apt-get install -y python3-pip
RUN apt-get install -y curl
RUN curl -fsSL https://deb.nodesource.com/setup_18.x | bash -
RUN apt-get install nodejs -y
COPY . /app
WORKDIR /app
RUN pip3 install -r requirements.txt
RUN cd /app/frontend
WORKDIR /app/frontend
RUN npm install
RUN npm run build
RUN cp -r /app/frontend/dist /app/static
WORKDIR /app
CMD ["gunicorn", "--bind", "0.0.0.0:5000", "app:app"]
