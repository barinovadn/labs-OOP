Write-Host "Installing Newman..." -ForegroundColor Green
npm install newman --save-dev

New-Item -ItemType Directory -Force -Path "results"

Write-Host "`nRunning Newman tests..." -ForegroundColor Green
npx newman run API_Tests.postman_collection.json -e environment.json --reporters cli,json --reporter-json-export results/results.json

if ($LASTEXITCODE -eq 0) {
    Write-Host "`nTests completed successfully!" -ForegroundColor Green
} else {
    Write-Host "`nTests failed!" -ForegroundColor Red
    exit 1
}