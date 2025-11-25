Write-Host "Building and starting Docker containers..." -ForegroundColor Green

Write-Host "Stopping and deleting existing containers..." -ForegroundColor Yellow
docker-compose down -v

Write-Host "Building and starting containers..." -ForegroundColor Yellow
docker-compose up --build -d --force-recreate

Write-Host "Waiting for application to start..." -ForegroundColor Yellow
Start-Sleep -Seconds 15

Write-Host "`nContainer status:" -ForegroundColor Green
docker-compose ps

Write-Host "`nApplication logs (last 20 lines):" -ForegroundColor Green
docker-compose logs --tail=20 app

Write-Host "`nTo view all logs, run: docker-compose logs -f app" -ForegroundColor Cyan
Write-Host "To stop containers, run: docker-compose down" -ForegroundColor Cyan
Write-Host "`nApplication should be available at: http://localhost:8080" -ForegroundColor Green