// server.ts
import { APP_BASE_HREF } from '@angular/common';
import { renderApplication } from '@angular/platform-server';
import { bootstrapApplication } from '@angular/platform-browser';
import express from 'express';
import { fileURLToPath } from 'node:url';
import { dirname, join, resolve } from 'node:path';
import { AppComponent } from './src/app/app.component';

// Create Express app
export function app(): express.Express {
  const server = express();

  const serverDistFolder = dirname(fileURLToPath(import.meta.url));
  const browserDistFolder = resolve(serverDistFolder, '../browser'); // adjust if your dist folder is different
  const indexHtml = join(browserDistFolder, 'index.html'); // the main browser index

  // Serve static files
  server.get('*.*', express.static(browserDistFolder, { maxAge: '1y' }));

  // All other routes use Angular Universal
  server.get('*', async (req, res, next) => {
    try {
      const html = await renderApplication(
        () => bootstrapApplication(AppComponent, {
          providers: [{ provide: APP_BASE_HREF, useValue: req.baseUrl }]
        }),
        {
          document: indexHtml,
          url: req.originalUrl
        }
      );
      res.send(html);
    } catch (err: any) {
      next(err);
    }
  });

  return server;
}

// Start server
function run(): void {
  const port = process.env['PORT'] || 4000;
  const server = app();
  server.listen(port, () => {
    console.log(`Node Express server listening on http://localhost:${port}`);
  });
}

run();
