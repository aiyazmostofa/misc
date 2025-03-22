FROM python:3
WORKDIR /app
RUN pip install --no-cache-dir discord.py-self
COPY bot.py .
COPY token .
CMD ["python", "bot.py"]
