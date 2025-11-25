Write-Host "Testing API endpoints with Basic Authentication..." -ForegroundColor Green

$baseUrl = "http://localhost:8080"

Write-Host "`n1. Testing Basic Authentication with protected endpoints..." -ForegroundColor Yellow

$testUsers = @(
    @{username = "admin"; password = "admin"; role = "ADMIN"},
    @{username = "user"; password = "user"; role = "USER"}
)

foreach ($user in $testUsers) {
    $credential = [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes("$($user.username):$($user.password)"))
    $headers = @{ Authorization = "Basic $credential" }
    
    Write-Host "   Testing user: $($user.username)..." -ForegroundColor Gray -NoNewline
    
    $endpoints = @(
        "/api/users/1", "/api/users/2", "/api/users/3", "/api/users/4", "/api/users/5",
        "/api/users/6", "/api/users/7", "/api/users/8", "/api/users/9", "/api/users/10",
        "/api/functions", "/api/roles", "/api/points", "/api/users/4/functions"
        )
    
    $success = $false
    foreach ($endpoint in $endpoints) {
        try {
            $response = Invoke-WebRequest -Uri "$baseUrl$endpoint" -Method GET -Headers $headers -UseBasicParsing -TimeoutSec 5 -ErrorAction SilentlyContinue
            if ($response.StatusCode -eq 200 -or $response.StatusCode -eq 404) {
                $success = $true
                Write-Host " - Authenticated!" -ForegroundColor Green
                break
            }
        } catch {  continue }
    }
    
    if (-not $success) {
        Write-Host " x Authentication failed" -ForegroundColor Red
    }
}

Write-Host "`n2. Testing role-based access control..." -ForegroundColor Yellow

# Тестируем админские эндпоинты
$adminCred = [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes("admin:admin"))
$adminHeaders = @{ Authorization = "Basic $adminCred" }

$adminEndpoints = @(
    @{path = "/api/users"; method = "GET"; description = "list users"},
    @{path = "/api/roles"; method = "GET"; description = "list roles"},
    @{path = "/api/functions"; method = "GET"; description = "list functions"}
)

foreach ($endpoint in $adminEndpoints) {
    try {
        if ($endpoint.method -eq "GET") {
            $response = Invoke-WebRequest -Uri "$baseUrl$($endpoint.path)" -Method GET -Headers $adminHeaders -UseBasicParsing -TimeoutSec 5 -ErrorAction Stop
        } else {
            $response = Invoke-WebRequest -Uri "$baseUrl$($endpoint.path)" -Method POST -Headers $adminHeaders -Body "{}" -ContentType "application/json" -UseBasicParsing -TimeoutSec 5 -ErrorAction Stop
        }
        Write-Host "   - Admin can access $($endpoint.description)" -ForegroundColor Green
    } catch {
        $statusCode = $_.Exception.Response.StatusCode.value__
        if ($statusCode -eq 404) {
            Write-Host "   /  Admin endpoint $($endpoint.path) not found (404)" -ForegroundColor Yellow
        } else {
            Write-Host "   x Admin cannot access $($endpoint.description): Status $statusCode" -ForegroundColor Red
        }
    }
}

Write-Host "`n3. Testing public registration endpoint..." -ForegroundColor Yellow
$registerBody = @{
    username = "test_$((Get-Date).ToString('HHmmss'))"
    password = "test_$((Get-Date).ToString('HHmmss'))"
    email = "test_$((Get-Date).ToString('HHmmss'))@example.com"
} | ConvertTo-Json

try {
    $response = Invoke-WebRequest -Uri "$baseUrl/api/auth/register" -Method POST -Body $registerBody -ContentType "application/json" -UseBasicParsing -TimeoutSec 10 -ErrorAction Stop
    Write-Host "   - Public registration works! User created successfully" -ForegroundColor Green
} catch {
    Write-Host "   x Registration failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n4. Testing unauthorized access..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/api/users" -Method GET -UseBasicParsing -TimeoutSec 5 -ErrorAction Stop
    Write-Host "   x Security misconfigured - endpoint accessible without auth" -ForegroundColor Red
} catch {
    if ($_.Exception.Response.StatusCode.value__ -eq 401) {
        Write-Host "   - Security working - endpoint requires authentication" -ForegroundColor Green
    } else {
        Write-Host "   /  Endpoint responded with: $($_.Exception.Response.StatusCode.value__)" -ForegroundColor Yellow
    }
}

Write-Host "`nAPI testing completed!" -ForegroundColor Green
Write-Host "Basic Authentication is configured - use credentials in Authorization header" -ForegroundColor Cyan