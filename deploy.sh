echo "Building app..."
./mvnw clean package

echo "Deploy files to server..."
scp -r -i ./swp target/petsocial.jar root@103.253.147.216:/var/www/pet-be/

ssh -i ./swp root@103.253.147.216 <<EOF
pid=\$(sudo lsof -t -i :8080)

if [ -z "\$pid" ]; then
    echo "Start server..."
else
    echo "Restart server..."
    sudo kill -9 "\$pid"
fi
cd /var/www/pet-be
java -jar petsocial.jar
EOF
exit
echo "Done!"