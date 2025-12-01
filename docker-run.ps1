Write-Host "Building and starting Docker containers..." -ForegroundColor Green

Write-Host "Stopping and deleting existing containers..." -ForegroundColor Yellow
docker rm -f labs-oop-app labs-oop-db 2>$null
docker-compose down -v

Write-Host "Building and starting containers..." -ForegroundColor Yellow
docker-compose up --build -d --force-recreate

Write-Host "Waiting for application to start..." -ForegroundColor Yellow
Write-Host "Waiting for application to be healthy..." -ForegroundColor Yellow

# Wait for container to be healthy
$maxAttempts = 30
$attempt = 0
$isHealthy = $false

while ($attempt -lt $maxAttempts -and -not $isHealthy) {
    $attempt++
    Start-Sleep -Seconds 2
    
    $health = docker inspect --format='{{.State.Health.Status}}' labs-oop-app 2>$null
    
    if ($health -eq "healthy") {
        $isHealthy = $true
        Write-Host "Application is healthy!" -ForegroundColor Green
    } else {
        Write-Host "Attempt $attempt/$maxAttempts - Status: $health" -ForegroundColor Yellow
    }
}

if (-not $isHealthy) {
    Write-Host "Warning: Application did not become healthy in time" -ForegroundColor Red
}

Write-Host "`nContainer status:" -ForegroundColor Green
docker-compose ps

Write-Host "`nApplication logs (last 20 lines):" -ForegroundColor Green
docker-compose logs --tail=20 app

Write-Host "`nTo view all logs, run: docker-compose logs -f app" -ForegroundColor Cyan
Write-Host "To stop containers, run: docker-compose down" -ForegroundColor Cyan
Write-Host "`nApplication should be available at: http://localhost:8080/labs-oop" -ForegroundColor Green