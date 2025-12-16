Write-Host "Building and starting Docker containers..." -ForegroundColor Green

# Always rebuild - stop and clean up existing containers
Write-Host "Stopping and cleaning up existing containers (preserving volumes)..." -ForegroundColor Yellow
docker-compose down --remove-orphans

Write-Host "Building and starting containers..." -ForegroundColor Yellow
docker-compose up --build -d

Write-Host "Waiting for containers to start..." -ForegroundColor Yellow

# First, check if database container started successfully
Start-Sleep -Seconds 3
$dbStatus = docker inspect --format='{{.State.Status}}' labs-oop-db 2>$null
$dbExitCode = docker inspect --format='{{.State.ExitCode}}' labs-oop-db 2>$null

# Check if database container failed to start
if ($dbStatus -eq "exited") {
    # Container exited - check if it was an error
    if ($dbExitCode -ne "0" -and $dbExitCode -ne "") {
        Write-Host "`nERROR: Database container failed to start!" -ForegroundColor Red
        Write-Host "Database container status: $dbStatus (exit code: $dbExitCode)" -ForegroundColor Red
        Write-Host "`nDatabase container logs:" -ForegroundColor Yellow
        docker logs labs-oop-db 2>&1 | Select-Object -Last 20
        
        # Check for common issues
        $dbLogs = docker logs labs-oop-db 2>&1 | Out-String
        if ($dbLogs -match "incompatible|version") {
            Write-Host "`nWARNING: Database version mismatch detected!" -ForegroundColor Yellow
            Write-Host "The existing database volume was created with a different PostgreSQL version." -ForegroundColor Yellow
            Write-Host "To preserve your data, you may need to:" -ForegroundColor Yellow
            Write-Host "  1. Update docker-compose.yml to match the existing PostgreSQL version, OR" -ForegroundColor Cyan
            Write-Host "  2. Migrate the database data to the new version" -ForegroundColor Cyan
            Write-Host "`nThe database volume is preserved and not deleted." -ForegroundColor Green
        }
        
        Write-Host "`nStopping app container..." -ForegroundColor Yellow
        docker stop labs-oop-app 2>$null
        exit 1
    }
}

Write-Host "Database container is running. Waiting for application to start..." -ForegroundColor Green

# Wait for container to be running and application to respond
$maxAttempts = 60
$attempt = 0
$isReady = $false
$appUrl = "http://localhost:8080"

while ($attempt -lt $maxAttempts -and -not $isReady) {
    $attempt++
    Start-Sleep -Seconds 2
    
    # Check if container is running
    $containerStatus = docker inspect --format='{{.State.Status}}' labs-oop-app 2>$null
    if ($containerStatus -ne "running") {
        Write-Host "Attempt $attempt/$maxAttempts - Container status: $containerStatus" -ForegroundColor Yellow
        continue
    }
    
    # Check if application is responding via HTTP
    try {
        $response = Invoke-WebRequest -Uri $appUrl -Method Get -TimeoutSec 2 -UseBasicParsing -ErrorAction Stop
        if ($response.StatusCode -eq 200) {
            $isReady = $true
            Write-Host "Application is ready and responding!" -ForegroundColor Green
        }
    } catch {
        # Try checking API endpoint as fallback
        try {
            $apiResponse = Invoke-WebRequest -Uri "$appUrl/api/users" -Method Get -TimeoutSec 2 -UseBasicParsing -ErrorAction Stop
            # Any response (even 401/403) means the app is up
            $isReady = $true
            Write-Host "Application is ready and responding!" -ForegroundColor Green
        } catch {
            Write-Host "Attempt $attempt/$maxAttempts - Waiting for application to respond..." -ForegroundColor Yellow
        }
    }
}

if (-not $isReady) {
    Write-Host "Warning: Application did not become ready in time, but it may still be starting up" -ForegroundColor Yellow
    Write-Host "Please check the logs below or try accessing $appUrl manually" -ForegroundColor Yellow
}

Write-Host "`nContainer status:" -ForegroundColor Green
docker-compose ps

Write-Host "`nDatabase logs (last 15 lines):" -ForegroundColor Green
docker-compose logs --tail=15 postgres

Write-Host "`nApplication logs (last 20 lines):" -ForegroundColor Green
docker-compose logs --tail=20 app

Write-Host "`nTo view all logs, run: docker-compose logs -f" -ForegroundColor Cyan
Write-Host "To stop containers (preserving volumes), run: docker-compose down" -ForegroundColor Cyan
Write-Host "To stop and remove volumes, run: docker-compose down -v" -ForegroundColor Yellow
Write-Host "`nApplication should be available at: http://localhost:8080/" -ForegroundColor Green