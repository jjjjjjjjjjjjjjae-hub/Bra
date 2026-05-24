import subprocess

print("=== 🔨 ГРАДЛДЫҢ ТОЛЫҚ ҚАТЕ ЖУРНАЛЫ ===")
gradle_cmd = "./gradlew assembleDebug --stacktrace"
result = subprocess.run(gradle_cmd, shell=True, capture_output=True, text=True)

print("\n--- [STDOUT] СОҢҒЫ ЖОЛДАРЫ ---")
out_lines = result.stdout.split('\n')
for line in out_lines[-25:]:
    print(line)

print("\n--- [STDERR] СОҢҒЫ ЖОЛДАРЫ ---")
err_lines = result.stderr.split('\n')
for line in err_lines[-25:]:
    print(line)

print("=======================================")
