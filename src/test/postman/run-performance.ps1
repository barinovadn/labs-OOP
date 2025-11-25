Write-Host "Installing dependencies..." -ForegroundColor Green
npm install

New-Item -ItemType Directory -Force -Path "results"

Write-Host "`nRunning performance tests..." -ForegroundColor Green
node performance-test.js

if ($LASTEXITCODE -eq 0) {
    Write-Host "`nPerformance tests completed!" -ForegroundColor Green
    Write-Host "Check results/PERFORMANCE_RESULTS.md for results" -ForegroundColor Yellow
} else {
    Write-Host "`nPerformance tests failed!" -ForegroundColor Red
    exit 1
}