Write-Host "Installing dependencies..." -ForegroundColor Green
npm install

Write-Host "`nRunning performance tests..." -ForegroundColor Green
node performance-test.js

if ($LASTEXITCODE -eq 0) {
    Write-Host "`nPerformance tests completed!" -ForegroundColor Green
    Write-Host "Check PERFORMANCE_RESULTS.md for results" -ForegroundColor Yellow
} else {
    Write-Host "`nPerformance tests failed!" -ForegroundColor Red
    exit 1
}


